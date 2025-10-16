# **AI Server (DEV)**

### **기능**
REST API Call 시에 AI Server 로의 API call 하고 Response 전달



### **API**
/v1/transcriptions  ==> (openAI Transcription)

/v1/task-plans  ==> (openAI chat)

/v1/text-detections  ==> (Google AI images:annotate)



### **Build 및 실행**
1. bootjar 생성 (obigo_ai_server-0.0.1-SNAPSHOT.jar)
2. 해당 repository 에 포함되어 있는 start_ai_server.sh 를 bootjar 실행할 위치에 같이 있도록 복사
3. ./start_ai_server.sh 실행
   
   > OPENAI_API_KEY 입력
   
   > GOOGLE_VISION_KEY 입력

   
** log 는 동일 폴더에 obigo_ai_server.log 로 저장됨.
