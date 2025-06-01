import sys
from PIL import Image
import numpy as np
import tensorflow as tf

# 1. 모델 로드 (구조+가중치 자동 포함)
model = tf.keras.models.load_model("model/keras_model.h5")

# 2. 이미지 전처리
image_path = sys.argv[1]
image = Image.open(image_path).convert("RGB")
image = image.resize((224, 224))  # → 학습할 때 사용한 input size와 일치시켜야 함
image_array = np.array(image) / 255.0
image_array = np.expand_dims(image_array, axis=0)  # (1, 224, 224, 3)

# 3. 예측
predictions = model.predict(image_array)
predicted_class = int(np.argmax(predictions))

# 4. 결과 출력 (Spring이 읽어갈 표준 출력)
print(predicted_class)
