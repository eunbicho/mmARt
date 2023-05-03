import os
import tensorflow as tf


MODELDIR=os.getcwd()+"/app/model/ns_bert_1_epoch"


def load_model():
    model = tf.saved_model.load(MODELDIR)
    return model