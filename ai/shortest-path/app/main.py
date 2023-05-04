from fastapi import FastAPI
from pydantic import BaseModel


# class Review(BaseModel):
#     review: str


app = FastAPI()


@app.get("/")
def find_shortest_path():
    return "server connection success"