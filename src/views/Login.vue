<template>
<div class="login-container">
    <div class="login-box">
      <h1 class="logo">MyApp</h1>
      <p class="welcome-text">로그인 후 서비스를 이용하세요.</p>
      
      <form @submit.prevent="checkBool">
        <input 
          :type="inputType"
          v-model="username" 
          :placeholder="id" 
          required
        />
        <input 
          type="password" 
          v-model="password" 
          :placeholder="pwd"
        />
        <button type="submit">{{ loginBtn }}</button>
      </form>

      <div class="links">
        <a href="signup">회원가입</a>
        <a href="#" @click="isBool">{{ findPwd }}</a>
      </div>
    </div>
    <div>
      <!-- <button @click="endCamera">카메라 끄기</button>
      <button @click="startCamera">카메라 켜기</button> -->
      <video ref="video" autoplay playsinline width="640" height="480"></video>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const URL = '/api/hr/login';
const URL_PASSWORD = '/api/hr';
const username = ref('');
const password = ref('');
const router = useRouter();
const bool = ref(false);
const inputType = ref('text');
const id = ref('아이디');
const pwd = ref('비밀번호');
const loginBtn = ref('로그인');
const findPwd = ref('비밀번호 찾기');
const certVal = ref('F'); //F 는 인증 번호, S는 인증 확인


const isBool = () => {
  console.log(bool.value)
  if(!bool.value){
    pwd.value = "인증번호 입력";
    password.value = '';
    inputType.value = "email";
    loginBtn.value = "인증번호 전송";
    id.value = "이메일 입력"
    findPwd.value = "로그인"
    certVal.value = "F";
    bool.value = true;
  }else{
    bool.value = false;
    password.value = '';
    pwd.value = "비밀번호 입력";
    inputType.value = "text";
    loginBtn.value = "로그인";
    id.value = "아이디"
    findPwd.value = "비밀번호 찾기"
  }
  
}

const checkBool = () => {
  if(!bool.value) {
    login();
  }else{
    if(certVal.value === "F"){
      handleSignup();
    }else{
      checkIn();
    }
    
  }
}

const MAIL_SERVER = '/api/mail/send';
const MAIL_SERVER_CERT = '/api/mail/verify';

const handleSignup = async () => {
  try {
    const to = encodeURIComponent(username.value);

    const response = await fetch(`${MAIL_SERVER}?to=${to}`, {
      method: "POST",
      // 쿼리 파라미터 방식이므로 Content-Type 필요 없음
    });

    loginBtn.value = "인증번호 확인";
    certVal.value = "S";

    if (!response.ok) {
      throw new Error("서버 오류: " + response.status);
    }

  } catch (error) {
    console.error("이메일 전송 실패:", error);
  }
};

const checkIn = async () => {
  if(password.value.length !== 6){
    alert('인증번호 6자리 입력해주세요!');
  }

  const certRes = await fetch(MAIL_SERVER_CERT, {
    method: "POST",
    headers:{
      "Content-Type": "application/json", // JSON 전송
    },
    body: JSON.stringify({
      username: username.value,
      password: password.value
    })});
    console.log(response)

    // .then(res => {console.log(res)})
    // .then(data => {
    //   if (data.status === 'fail') {
    //     throw new Error("인증번호 오류: " + data.message);
    //   } else {
    //     console.log("인증완료");
    //   }
    // })
    
  const response = await fetch(URL_PASSWORD + `/${username.value}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json", // JSON 전송
    },
    body: JSON.stringify({
      username: username.value
    })
  }).then(res => res.json())
    .then(data => {
      if (data.status === 'fail') {
        throw new Error("로그인 오류: " + data.message);
      } else {
        alert('인증되었습니다. 비밀번호는 1111로 초기화 되었습니다.');
        isBool();
        username.value = '';
        password.value = '';
      }
    })
    .catch(err => {
      console.error(err.message);
      alert(err.message);
    });
}

const login = async () => {
  try {
    console.log(username.value)
    const response = await fetch(URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json", // JSON 전송
      },
      // credentials: "include", // 쿠키 인증 필요 시
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    }).then(res => res.json())
      .then(data => {
        if (data.status === 'fail') {
          throw new Error("로그인 오류: " + data.message);
        } else {
          router.push('/main'); // 로그인 성공 시 이동
        }
      })
      .catch(err => {
        console.error(err.message);
        alert(err.message);
      });
  } catch (error) {
    console.error("로그인 실패:", error);
  }
};



// const video = ref(null);
// const cameraOn = ref(false);

// const endCamera = () => {
//   router.push('/');
// };

// const startCamera = async () => {
//   try {
//     const stream = await navigator.mediaDevices.getUserMedia({ video: true });
//     video.value.srcObject = stream;
//     cameraOn.value = true; // 웹캠 화면 보이도록 상태 변경
//   } catch (err) {
//     console.error("카메라 접근 실패:", err);
//     alert("카메라 접근 권한이 필요합니다.");
//   }
// };

</script>

<style scoped>
/* 전역 스타일 초기화 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 전체화면 배경, 오버플로우 제거 */
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  overflow: hidden; /* 스크롤 완전 제거 */
  
  /* 로그인 박스를 우측 20% 지점에 배치 */
  display: flex;
  align-items: center;
  justify-content: center;
  /* padding-left: 40%;  */
  /* 좌측에서 20% 지점 */
}

/* 로그인 박스 */
.login-box {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 50px 40px;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  width: 420px;
  max-width: 90vw;
  text-align: center;
  position: relative;
  overflow: hidden;
  animation: slideInRight 0.8s ease-out;
}

/* 로그인 박스 상단 라인 효과 */
.login-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #667eea, #764ba2);
}

/* 로그인 박스 글래스모피즘 효과 */
.login-box::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, 
    rgba(255, 255, 255, 0.1) 0%, 
    rgba(255, 255, 255, 0.05) 100%);
  pointer-events: none;
  z-index: -1;
}

/* 로고 */
.logo {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 안내 텍스트 */
.welcome-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 35px;
  line-height: 1.5;
}

/* 폼 스타일 */
form {
  width: 100%;
}

/* input 스타일 */
input {
  width: 100%;
  padding: 18px 22px;
  margin-bottom: 20px;
  border: 2px solid rgba(230, 230, 230, 0.5);
  border-radius: 15px;
  font-size: 16px;
  transition: all 0.3s ease;
  background: rgba(248, 249, 250, 0.8);
  outline: none;
}

input:focus {
  border-color: #667eea;
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

input::placeholder {
  color: #adb5bd;
}

/* 버튼 스타일 */
button {
  width: 100%;
  padding: 18px;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: #fff;
  border: none;
  border-radius: 15px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.4);
}

button:active {
  transform: translateY(0);
}

/* 하단 링크 */
.links {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  padding-top: 25px;
  border-top: 1px solid rgba(241, 243, 244, 0.8);
}

.links a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s ease;
  cursor: pointer;
}

.links a:hover {
  color: #764ba2;
  text-decoration: underline;
  transform: translateY(-1px);
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .login-container {
    justify-content: center; /* 작은 화면에서는 중앙 배치 */
    padding-left: 0;
  }
}

@media (max-width: 768px) {
  .login-box {
    padding: 40px 30px;
    width: 350px;
  }
  
  .logo {
    font-size: 30px;
  }
  
  input, button {
    padding: 16px 20px;
    font-size: 15px;
  }
}

@media (max-width: 480px) {
  .login-box {
    padding: 35px 25px;
    width: 320px;
    margin: 20px;
  }
  
  .logo {
    font-size: 28px;
  }
  
  input, button {
    padding: 14px 18px;
    font-size: 14px;
  }
}

/* 애니메이션 */
@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(100px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 추가 스타일: 배경 애니메이션 효과 */
.login-container::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.1) 0%,
    transparent 50%
  );
  animation: rotate 20s linear infinite;
  pointer-events: none;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}


</style>