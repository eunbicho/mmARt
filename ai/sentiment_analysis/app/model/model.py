import os
import tensorflow as tf
import tensorflow_hub as hub
import tensorflow_text as text


MODELDIR=os.getcwd()+"/app/model/ns_bert_1_epoch"


def load_model():
    model = tf.saved_model.load(MODELDIR)
    return model