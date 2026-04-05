import sys
import cv2
import numpy as np
import joblib
import os
from skimage import feature

# SINCRONIZAÇÃO: Estes valores devem ser IDÊNTICOS ao seu train.py (Versão Hyper-Augmentation)
RADIUS = 3
N_POINTS = 8 * RADIUS

def extract_lbp_features(image_path):
    # Carrega em escala de cinza conforme o treinamento
    img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    if img is None:
        raise ValueError(f"Não foi possível ler a imagem: {image_path}")

    # PADRONIZAÇÃO: Redimensionar para 200x200 (Essencial para manter a escala do LBP)
    img = cv2.resize(img, (200, 200))

    # Extração LBP Uniforme
    lbp = feature.local_binary_pattern(img, N_POINTS, RADIUS, method="uniform")

    # Gerar histograma (deve resultar em 26 características para RADIUS=3)
    (hist, _) = np.histogram(lbp.ravel(), bins=np.arange(0, N_POINTS + 3), range=(0, N_POINTS + 2))

    # Normalização do histograma
    hist = hist.astype("float")
    hist /= (hist.sum() + 1e-7)
    return hist

if __name__ == "__main__":
    # Garante que o Java enviou o caminho da imagem temporária
    if len(sys.argv) < 2:
        print("ERRO: Caminho da imagem não fornecido.")
        sys.exit(1)

    image_path = sys.argv[1]

    try:
        # 1. Extração de características da imagem enviada pelo Swagger
        features = extract_lbp_features(image_path)

        # 2. Carregamento do Modelo de Elite
        # Ajustado: Usando o caminho absoluto direto para evitar conflitos de diretório de trabalho do Java
        model_path = r'C:\Users\Mateus\Desktop\ProjetoJava\fish_cv_model3.pkl'

        if not os.path.exists(model_path):
            raise FileNotFoundError(f"Modelo não encontrado em: {model_path}")

        model = joblib.load(model_path)

        # 3. Predição (Classificação)
        prediction = model.predict([features])[0]

        # 4. Resposta Final: O Java (FishOrchestratorService) capturará este print
        if prediction == 0:
            print("FRESCO")
        else:
            print("ESTRAGADO")

    except Exception as e:
        # Se houver erro, o Java capturará a mensagem e retornará no Response Body
        print(f"ERRO: {str(e)}")
        sys.exit(1)