# Configuração do Servidor

O mod gera o arquivo de configuração em `config/serverpackswitcher.json` dentro da pasta do servidor. Os pacotes podem ser configurados através de **URLs Diretas** ou **links do Modrinth**.

---

## 🔧 Modelo JSON
Exemplo de configuração contendo entradas do Modrinth e links diretos:

```json
{
  "packs": {
    "glassdoors": {
      "modrinth": "https://modrinth.com/resourcepack/glass-doors",
      "required": false,
      "prompt": "Would you like to apply the Glass Doors resource pack?"
    },
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to apply the Fresh Animations resource pack?"
    },
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

---

## 📋 Campos de Configuração
Para cada entrada em `"packs"`, é possível configurar:

* **`modrinth`** (String opcional): URL do Modrinth (ex: `https://modrinth.com/resourcepack/glass-doors`), slug do projeto (ex: `glass-doors`) ou link de versão específica. Quando preenchido, o servidor busca automaticamente o link de download direto e o hash SHA-1 de forma assíncrona pela API do Modrinth ao inicializar.
* **`url`** (Obrigatório se `modrinth` não for informado): Link de download direto para o arquivo `.zip` do pacote.
* **`hash`** (String opcional): Hash SHA-1 do arquivo zip (40 caracteres hexadecimais). Altamente recomendado para links diretos para habilitar cache no cliente.
* **`required`** (Booleano opcional, padrão: `false`): Se `true`, o cliente é obrigado a aceitar o pacote para continuar no servidor.
* **`prompt`** (String opcional): Mensagem personalizada exibida na tela do jogador ao solicitar o download do pacote.
* **`id`** (UUID opcional): Identificador único do pacote. Se omitido, é gerado automaticamente.

---

## 🔍 Como obter URLs e calcular hashes (Links Diretos)

Se você não for usar o Modrinth e preferir hospedar links diretos:

### 1. Obter uma URL de Download Direto
A URL deve iniciar o download do arquivo `.zip` imediatamente ao ser acessada, sem abrir páginas intermediárias.
- **Discord**: Envie o arquivo `.zip` para um canal, clique com o botão direito no ícone de download e selecione **Copiar link**.
- **Dropbox**: Copie o link de compartilhamento e altere o final de `?dl=0` para `?raw=1`.
- **GitHub**: Utilize o link de download direto do anexo de um Release.

### 2. Gerar o hash SHA-1
O hash é uma impressão digital única de 40 caracteres que permite ao Minecraft manter o pacote em cache:
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **Ferramentas Online**: Arraste seu arquivo `.zip` para uma calculadora de checksum SHA-1 online.
