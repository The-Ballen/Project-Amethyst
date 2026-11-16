# Project Amethyst

Implementação de um simulador de Máquina de Turing determinística de fita única em Java, com suporte a definição de regras e carregamento de funções de transição a partir de arquivos externos.

## Pré-requisitos

* **Java Development Kit (JDK):** Versão 17 ou superior.

Para verificar se o Java já está instalado corretamente, execute:

```bash
java -version
```

## Compilação

A compilação e empacotamento é feita automaticamente via Gradle Wrapper. Na raiz do projeto, execute o comando correspondente ao seu sistema operacional para compilar o código-fonte e gerar o pacote `.jar` executável:

### Windows

```powershell
.\gradlew.bat build
```

### Linux / macOS (Bash)

Caso seja a primeira execução, garanta permissão de execução ao script e inicie o build:

```bash
chmod +x ./gradlew
./gradlew build
```

## Execução

Para executar o pacote compilado, utilize o comando `java -jar` apontando para o arquivo gerado:

### Windows (PowerShell)

Para a correta exibição de símbolos matemáticos no console do Windows, configure a codificação de saída para UTF-8 antes de executar:

```powershell
# Configura a codificação de saída como UTF-8
[console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Executa o pacote compilado
java -jar .\build\libs\Automata.jar
```

### Windows (CMD)

```cmd
:: Configura a codificação de saída como UTF-8
chcp 65001

:: Executa o pacote compilado
java -jar .\build\libs\Automata.jar
```

### Linux / macOS (Bash)

```bash
java -jar ./build/libs/Automata.jar
```

## Licença

Distribuído sob a licença <a href="https://www.gnu.org/licenses/gpl-3.0">GNU General Public License v3.0</a>.
