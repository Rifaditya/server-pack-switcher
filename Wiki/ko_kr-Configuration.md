# 서버 구성 설정

모드는 서버 디렉터리의 `config/serverpackswitcher.json` 경로에 구성 설정 파일을 생성합니다. 리소스 팩은 **직접 다운로드 URL** 또는 **Modrinth 링크**를 통해 구성할 수 있습니다.

---

## 🔧 JSON 템플릿 예시
Modrinth 링크와 직접 URL 설정을 함께 보여주는 예제 템플릿입니다:

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

## 📋 구성 항목 설명
`"packs"` 항목 아래 정의할 수 있는 세부 설정입니다:

* **`modrinth`** (선택 문자열): Modrinth 프로젝트 URL(예: `https://modrinth.com/resourcepack/glass-doors`), 프로젝트 슬러그(예: `glass-doors`), 또는 특정 버전 링크. 지정할 경우 서버 시작 시 백그라운드 스레드에서 Modrinth API를 통해 직접 다운로드 링크와 SHA-1 해시를 비동기식으로 자동 조회합니다.
* **`url`** (`modrinth`를 생략할 경우 필수): 리소스 팩 `.zip` 파일의 직접 다운로드 링크.
* **`hash`** (선택 문자열): zip 파일의 SHA-1 해시 코드 (40자리 16진수). Modrinth를 사용하지 않는 경우 클라이언트 캐싱 활성화를 위해 강력히 권장됩니다.
* **`required`** (선택 불리언, 기본값: `false`): `true`로 설정하면 클라이언트에 팩 수락을 강제하는 대화 상자가 나타납니다.
* **`prompt`** (선택 문자열): 팩 다운로드 요청 시 플레이어 화면에 표시될 사용자 지정 안내 메시지.
* **`id`** (선택 UUID 문자열): 팩의 고유 식별자. 생략 시 자동으로 생성됩니다.

---

## 🔍 직접 다운로드 URL 및 SHA-1 해시 추출 방법

Modrinth를 사용하지 않고 직접 링크를 구성하려는 경우:

### 1. 직접 다운로드 URL 얻기
URL은 클릭 시 웹페이지 로딩 없이 즉시 `.zip` 파일이 바로 다운로드되어야 합니다.
- **Discord**: 리소스 팩 `.zip` 파일을 디스코드 채널에 업로드한 뒤, 다운로드 화살표 아이콘을 우클릭하고 **링크 복사**를 클릭합니다.
- **Dropbox**: 공유 링크를 복사한 뒤 끝부분의 `?dl=0`을 `?raw=1`로 변경합니다.
- **GitHub**: 릴리스 자산(Release Asset)의 직접 다운로드 링크를 사용합니다.

### 2. SHA-1 해시 추출하기
해시는 마인크래프트 클라이언트가 팩을 로컬에 캐시하여 재다운로드를 방지하도록 돕는 40자리 고유 식별 코드입니다:
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (터미널)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **온라인 도구**: 온라인 SHA-1 체크섬 계산기 웹사이트에 `.zip` 파일을 드래그 앤 드롭합니다.
