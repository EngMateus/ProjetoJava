import os
import cv2
import numpy as np
import joblib
from skimage import feature
from sklearn.ensemble import RandomForestClassifier

RADIUS = 3
N_POINTS = 8 * RADIUS

def extract_lbp_features_from_img(img):
    img = cv2.resize(img, (200, 200))
    lbp = feature.local_binary_pattern(img, N_POINTS, RADIUS, method="uniform")
    hist, _ = np.histogram(lbp.ravel(), bins=np.arange(0, N_POINTS + 3), range=(0, N_POINTS + 2))
    hist = hist.astype("float")
    hist /= (hist.sum() + 1e-7)
    return hist

def get_augmented_features(image_path):
    img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    if img is None: return []

    variants = []
    # Versões Geométricas
    variants.append(img) # Original
    variants.append(cv2.flip(img, 1)) # Espelhado
    variants.append(cv2.rotate(img, cv2.ROTATE_90_CLOCKWISE))

    # Versões de Iluminação (Crucial para generalizar)
    # 1. Mais brilho
    variants.append(cv2.convertScaleAbs(img, alpha=1.2, beta=30))
    # 2. Menos brilho
    variants.append(cv2.convertScaleAbs(img, alpha=0.8, beta=-30))
    # 3. Alto Contraste
    variants.append(cv2.convertScaleAbs(img, alpha=1.5, beta=0))

    # Versão com Ruído (Simula granulação da câmera)
    noise = np.random.normal(0, 5, img.shape).astype(np.uint8)
    variants.append(cv2.add(img, noise))

    return [extract_lbp_features_from_img(v) for v in variants]

def train():
    X, y = [], []
    classes = {"dataservice/dataset/Fresco": 0, "dataservice/dataset/Estragado": 1}

    print("🚀 Iniciando Hyper-Augmentation...")
    for folder, label in classes.items():
        if not os.path.exists(folder): continue
        for filename in os.listdir(folder):
            path = os.path.join(folder, filename)
            features_list = get_augmented_features(path)
            for f in features_list:
                X.append(f)
                y.append(label)

    if not X: return

    # RandomForest com 200 árvores para maior estabilidade
    model = RandomForestClassifier(n_estimators=200, max_depth=15, random_state=42)
    model.fit(np.array(X), np.array(y))

    model_path = r'C:\Users\Mateus\Desktop\ProjetoJava\fish_cv_model3.pkl'
    joblib.dump(model, model_path)
    print(f"✅ Sucesso! O modelo agora 'viu' {len(X)} variações das suas fotos.")

if __name__ == "__main__":
    train()