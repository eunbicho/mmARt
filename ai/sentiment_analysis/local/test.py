from model import load_model
import tensorflow as tf
import tensorflow_hub as hub
import tensorflow_text as text


model = load_model()


prediction = tf.sigmoid(model(tf.constant(['배송도 빠르고 제품도 좋네요'])))
print(prediction)