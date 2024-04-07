# T.G.winG Tech Blog API Server

경희대학교 컴퓨터공학과 학술동아리 티지윙의 테크 블로그 API 서버입니다. 

## git 브랜치 전략

`dev` 브렌치에서 기능별로 `feature` 브랜치 생성 후 기능이 완성되면 `dev` 브랜치에 `merge` 합니다. 

`dev` 브랜치에서 기능 개발을 완료하면 `main` 브랜치에 `merge` 하여 실제 서비스할 수 있도록 배포합니다. 

ex) `dev`에서 `feature/blog` 브랜치 생성 후 기능을 개발하고 다시 `dev`에 머지
