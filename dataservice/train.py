import os
import cv2
import numpy as np
import joblib
from skimage import feature
from sklearn.linear_model import LogisticRegression

RADIUS = 2
N_POINTS = 8 * RADIUS

def extract_lbp_features(image_path):
    img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    if img is None:
        raise ValueError(f"Erro ao carregar imagem: {image_path}")
    lbp = feature.local_binary_pattern(img, N_POINTS, RADIUS, method="uniform")
    hist, _ = np.histogram(lbp.ravel(), bins=np.arange(0, N_POINTS + 3), range=(0, N_POINTS + 2))
    hist = hist.astype("float")
    hist /= (hist.sum() + 1e-7)
    return hist

def train():
    X, y = [], []
    # ADAPTAÇÃO: Caminhos relativos para a base de dados
    classes = {"dataset/Fresco": 0, "dataset/Estragado": 1}

    for folder, label in classes.items():
        if not os.path.exists(folder):
            print(f"Pasta ignorada (não encontrada): {folder}")
            continue
        for filename in os.listdir(folder):
            path = os.path.join(folder, filename)
            try:
                X.append(extract_lbp_features(path))
                y.append(label)
            except Exception as e:
                print(f"Erro em {path}: {e}")

    if not X:
        print("Dataset vazio. Treinamento cancelado.")
        return

    model = LogisticRegression(random_state=42, max_iter=1000, C=0.01, solver="liblinear")
    model.fit(np.array(X), np.array(y))

    # Salva na raiz para o predict.py encontrar
    joblib.dump(model, 'fish_cv_model3.pkl')
    print("Sucesso: fish_cv_model3.pkl gerado na raiz.")

if __name__ == "__main__":
    train()