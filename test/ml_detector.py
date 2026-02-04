import pickle
import os

BASE = os.path.dirname(os.path.abspath(__file__))
MODEL = os.path.join(BASE, "model.pkl")

with open(MODEL, "rb") as f:
    VECTORIZER, MODEL_OBJ = pickle.load(f)

def predict_attack(payload):
    X = VECTORIZER.transform([payload])
    label = MODEL_OBJ.predict(X)[0]
    prob = max(MODEL_OBJ.predict_proba(X)[0])

    return {"ai_label": label, "confidence": round(float(prob), 2)}
