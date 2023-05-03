from fastapi import FastAPI
from app.model.model import load_model
import tensorflow as tf
from pydantic import BaseModel


class Review(BaseModel):
    review: str


app = FastAPI()


model = load_model()


@app.post("/reviews/sentiment-analysis")
def post_predict(review: Review):
    result = tf.sigmoid(model(tf.constant([review.review])))

    score = result.numpy()[0][0]

    prediction=-1

    if(score>=0.5):
        prediction=1
    else:
        prediction=0

    return prediction