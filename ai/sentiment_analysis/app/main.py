from fastapi import FastAPI


app = FastAPI()


@app.get("/")
def connect():
    return "server connection success"