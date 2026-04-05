# 1. Base: Java 21
FROM eclipse-temurin:21-jdk-jammy

# 2. Instalar Python 3.13 e ferramentas de isolamento
RUN apt-get update && apt-get install -y software-properties-common \
    && add-apt-repository ppa:deadsnakes/ppa \
    && apt-get update && apt-get install -y \
    python3.13 python3.13-dev python3.13-venv \
    libgl1-mesa-glx libglib2.0-0 \
    && rm -rf /var/lib/apt/lists/*

# 3. Diretório de trabalho
WORKDIR /app

# 4. Criar um Ambiente Virtual interno para o Python 3.13
# Isso resolve o erro de 'distutils' e garante o pip limpo
RUN python3.13 -m venv /app/venv
ENV PATH="/app/venv/bin:$PATH"

# 5. Copiar arquivos
COPY dataservice/predict.py /app/dataservice/
COPY fish_cv_model3.pkl /app/dataservice/
COPY dataservice/requirements.txt /app/requirements.txt
COPY dataservice/target/dataservice-0.0.1-SNAPSHOT.jar /app/app.jar

# 6. Instalar bibliotecas usando o pip do ambiente virtual
RUN pip install --upgrade pip setuptools wheel
RUN pip install --no-cache-dir -r /app/requirements.txt

# 7. Porta e Inicialização
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]