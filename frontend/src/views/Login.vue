<template>
<div class="login-container">
    <div class="login-box">
      <h1 class="logo">MyApp</h1>
      <p class="welcome-text">로그인 후 서비스를 이용하세요.</p>
      
      <form @submit="goToMain">
        <input 
          type="text" 
          v-model="username" 
          placeholder="아이디" 
          required
        />
        <input 
          type="password" 
          v-model="password" 
          placeholder="비밀번호" 
          required
        />
        <button type="submit">로그인</button>
      </form>

      <div class="links">
        <a href="#">회원가입</a>
        <a href="#">비밀번호 찾기</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const username = ref('');
const password = ref('');
const router = useRouter();

function goToMain() {
  console.log('hello')
  router.push('/main') // /main 화면으로 이동
}

// export default {
//   name: 'Login',
//   data() {
//     return {
//       username: '',
//       password: ''
//     }
//   },
//   methods: {
//     handleLogin() {
//       router.push('/downtime');
//       // 여기서 실제 로그인 로직 추가
//     //   alert(`아이디: ${this.username}, 비밀번호: ${this.password}`);
//     }
//   }
// }

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
  justify-content: flex-start;
  padding-left: 40%; /* 좌측에서 20% 지점 */
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