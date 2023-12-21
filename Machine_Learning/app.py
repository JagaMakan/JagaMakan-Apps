import os
from flask import (
    Flask,
    request,
    redirect,
    url_for,
    render_template,
    send_from_directory,
    flash,
    jsonify,
)
from werkzeug.utils import secure_filename
from PIL import Image
import numpy as np
import tensorflow_hub as hub
from object_detection.utils import label_map_util
from object_detection.utils import visualization_utils as viz_utils
from datetime import datetime
import csv

app = Flask(__name__)

app.config["ALLOWED_EXTENSIONS"] = set(["png", "jpg", "jpeg"])
app.config["UPLOAD_FOLDER"] = "uploads/"
app.config["DOWNLOAD_FOLDER"] = "downloads/"

LABEL_FILENAME = "labels/labelmap.pbtxt"
category_index = label_map_util.create_category_index_from_labelmap(
    LABEL_FILENAME, use_display_name=True
)

print(
    """
 _                    _ _               __  __           _      _             
| |    ___   __ _  __| (_)_ __   __ _  |  \/  | ___   __| | ___| |            
| |   / _ \ / _` |/ _` | | '_ \ / _` | | |\/| |/ _ \ / _` |/ _ \ |            
| |__| (_) | (_| | (_| | | | | | (_| | | |  | | (_) | (_| |  __/ |  _   _   _ 
|_____\___/ \__,_|\__,_|_|_| |_|\__, | |_|  |_|\___/ \__,_|\___|_| (_) (_) (_)
                                |___/                                         
"""
)
model = "saved_model/"
hub_model = hub.load(model)
print(
    """
 __  __           _      _   _                    _          _   _ 
|  \/  | ___   __| | ___| | | |    ___   __ _  __| | ___  __| | | |
| |\/| |/ _ \ / _` |/ _ \ | | |   / _ \ / _` |/ _` |/ _ \/ _` | | |
| |  | | (_) | (_| |  __/ | | |__| (_) | (_| | (_| |  __/ (_| | |_|
|_|  |_|\___/ \__,_|\___|_| |_____\___/ \__,_|\__,_|\___|\__,_| (_)
"""
)


def allowed_file(filename):
    return (
        "." in filename
        and filename.rsplit(".", 1)[1] in app.config["ALLOWED_EXTENSIONS"]
    )


def load_image_into_numpy_array(image):
    (image_width, image_height) = image.size
    return (
        np.array(image.getdata())
        .reshape((1, image_height, image_width, 3))
        .astype(np.uint8)
    )


def data_predict(detection_scores, detection_classes):
    mappings = {
        1: {"name": "nasi", "calories_per_item": 204},
        2: {"name": "telur_balado", "calories_per_item": 71},
        3: {"name": "tempe", "calories_per_item": 34},
        4: {"name": "tahu_goreng", "calories_per_item": 35},
        5: {"name": "mangkuk_bakso", "calories_per_item": 218},
    }

    detect_threshold = detection_scores > 0.8
    num_detected = len(detection_scores[detect_threshold])
    detected_class = detection_classes[:num_detected]
    arr_int = detected_class.round().astype(int)

    result_dict = []
    for item in np.unique(arr_int):
        quantity = np.count_nonzero(arr_int == item)
        food_item = mappings[item]
        food_name = food_item["name"]
        calories_per_item = food_item["calories_per_item"]
        total_calories = quantity * calories_per_item
        result_dict.append(
            {
                "name": food_name,
                "quantity": quantity,
                "calories": total_calories,
            }
        )

    return result_dict


@app.route("/")
def index():
    hello_json = {
        "status_code": 200,
        "message": "Success testing the JagaMakan API",
        "data": [],
    }
    return jsonify(hello_json)


@app.route("/predict", methods=["POST"])
def predict():
    file = request.files["file"]
    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config["UPLOAD_FOLDER"], filename))
        image_path = Image.open(file)
        image_path = image_path.convert("RGB")
        image_np = load_image_into_numpy_array(image_path)
        flip_image_horizontally = False
        convert_image_to_grayscale = False
        if flip_image_horizontally:
            image_np[0] = np.fliplr(image_np[0]).copy()
        if convert_image_to_grayscale:
            image_np[0] = np.tile(
                np.mean(image_np[0], 2, keepdims=True), (1, 1, 3)
            ).astype(np.uint8)
        results = hub_model(image_np)
        result = {key: value.numpy() for key, value in results.items()}
        # print(result)
        label_id_offset = 0
        image_np_with_detections = load_image_into_numpy_array(image_path)
        viz_utils.visualize_boxes_and_labels_on_image_array(
            image_np_with_detections[0],
            result["detection_boxes"][0],
            (result["detection_classes"][0] + label_id_offset).astype(int),
            result["detection_scores"][0],
            category_index,
            use_normalized_coordinates=True,
            max_boxes_to_draw=200,
            line_thickness=5,
            min_score_thresh=0.8,
            agnostic_mode=False,
        )
        # label = viz_utils.visualize_boxes_and_labels_on_image_array.class_name
        # print(label)

        try:
            detection_scores = result["detection_scores"][0]
            detection_classes = result["detection_classes"][0]
            label = data_predict(detection_scores, detection_classes)
            print(label)
        except:
            error = [datetime.now(), filename]
            with open("error_log.csv", "a+") as csvfile:
                csvwriter = csv.writer(csvfile)
                csvwriter.writerow(error)
            csvfile.close()
            label = "Tidak Terdeteksi"
        finally:
            predicted_image = Image.fromarray(image_np_with_detections.squeeze())
            predicted_image.save("downloads/" + filename)
            json = {
                "detected_label": label,
                "image-url": "nama-api-save-image/"
                + filename,  # ini aku kurang ngerti seharusnya cc lebih ngerti ini apa
            }
            return jsonify(json)
    else:
        json = {
            "data": [],
            "message": "Please upload JPG, JPEG, or PNG!",
            "error": "The selected file isn't supported!",
        }
    return jsonify(json)


@app.route("/downloads/<name>")
def download_file(name):
    return send_from_directory(app.config["DOWNLOAD_FOLDER"], name)


@app.errorhandler(404)
def not_found(error):
    return jsonify({"message": "Endpoint not found", "status_code": 404})


def create_app():
    return app


if __name__ == "__main__":
    app.run(debug=False, host="0.0.0.0", port=443)  # harusnya ada ssl key
