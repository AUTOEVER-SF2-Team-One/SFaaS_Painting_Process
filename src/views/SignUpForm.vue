<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="logo">MyApp</h1>
      <p class="welcome-text">회원가입 정보를 입력하세요.</p>

      <form @submit.prevent="handleSignup">
        <input 
          type="text" 
          v-model="id" 
          placeholder="아이디" 
          required
        />
        <input 
          type="email" 
          v-model="email" 
          placeholder="이메일" 
          required
        />
        <input 
          type="text" 
          v-model="username" 
          placeholder="이름" 
          required
        />
        <input 
          type="password" 
          v-model="password" 
          placeholder="비밀번호" 
          required
        />
        <input 
          type="password" 
          v-model="mailCert" 
          placeholder="이메일 인증 확인" 
          required
        />

        <!-- 이메일 인증 버튼 (비활성화 + 타이머 표시) -->
        <button 
          type="button" 
          @click="sendCert"
        >
          {{ isSent ? "인증번호 확인" : "이메일 인증" }}
        </button>
        <span v-if="isSent" class="timer-text">
          {{ remainingTime }}초 ⏳ 인증번호 확인
        </span>
      </form>
      
      <div class="links">
        <a href="/">로그인으로 돌아가기</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const id = ref('')
const username = ref('')
const email = ref('')
const password = ref('')
const mailCert = ref("")

const MAIL_SERVER = 'http://127.0.0.1:8000/send-email';
const isSent = ref(false)
const remainingTime = ref(60) // 1분 = 60초
let timer = null

// 회원가입 요청 (이메일 전송 포함)
const handleSignup = async () => {
  try {
    const response = await fetch(MAIL_SERVER, {
      method: "POST",
      headers: {
        "Content-Type": "application/json", 
      },
      body: JSON.stringify({
        to_email: email.value,
        subject: "MES 인증 번호",
        body: `${username.value}님은 265544 입니다.`
      })
    });

    if (!response.ok) {
      throw new Error("서버 오류: " + response.status);
    }

    const result = await response.json();
    console.log("이메일 전송 성공:", result);

  } catch (error) {
    console.error("이메일 전송 실패:", error);
  }
};

// 인증 버튼 클릭 시 → 버튼 비활성화 + 타이머 표시
const sendCert = () => {
  if(isSent.value){
    if('265544' === mailCert.value){
      alert('인증되었습니다.');
      router.push('/');
    }else{
      alert('인증번호가 틀렸습니다.');
    }
  }else{
    isSent.value = true
    remainingTime.value = 60

    // 타이머 시작
    timer = setInterval(() => {
      if (remainingTime.value > 0) {
        remainingTime.value--
      } else {
        clearInterval(timer)
        isSent.value = false // 시간이 끝나면 다시 인증 버튼 활성화
      }
    }, 1000)

    // 버튼 클릭하면 이메일도 전송
    handleSignup()
  }

  
}
</script>
<style scoped>
/* 기존 로그인 디자인 그대로 재사용 가능 */
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
}

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
  animation: slideInRight 0.8s ease-out;
}

.login-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #667eea, #764ba2);
}

.logo {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.welcome-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 35px;
}

input {
  width: 100%;
  padding: 18px 22px;
  margin-bottom: 20px;
  border: 2px solid rgba(230, 230, 230, 0.5);
  border-radius: 15px;
  font-size: 16px;
  background: rgba(248, 249, 250, 0.8);
  outline: none;
  transition: all 0.3s ease;
}

input:focus {
  border-color: #667eea;
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

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
}

button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.4);
}

.links {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  font-size: 14px;
}

.links a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.links a:hover {
  color: #764ba2;
  text-decoration: underline;
}

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

.timer-box {
  display: flex;
  gap: 8px;
  align-items: center;
  font-weight: bold;
  color: #d9534f;
}
</style>
