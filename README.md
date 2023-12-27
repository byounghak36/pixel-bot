## 개발 하기전 꼭 읽어주세요

### 1. properties 파일 명 변경 
clone 후 `/src/main/resources/pixel.properties.bak` 파일을 `pixel.properties` 파일 명으로 변경 하셔야 합니다.
### 2. 봇 토큰 및 넥슨 API 키 설정 
`pixel.properties` 파일에는 다음과 같은 내용이 있으며 

`botToken` 은 Discord Bot 토큰, `mapleAPIKey` 는 넥슨 OpenAPI 키 입니다.

#### 넥슨 API 키는 [넥슨 개발자 센터](https://openapi.nexon.com/) 에서 발급 받으실 수 있습니다.

```
# Discord Bot Token
botToken =

# Nexon OpenAPI
mapleAPIServer = https://open.api.nexon.com/maplestory/v1
mapleAPIKey =

```

## 환경 구성
- ### JDK 17
- ### 5.0.0-beta.18

