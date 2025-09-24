<template>
  <header class="header">
    <h1 class="logo">HYUNDAI</h1>
    <nav class="nav">
      <div class="menu-left">
        <NavButton text="Main" to="/main" />
        <NavButton text="Detail" to="/detail" />
        <NavButton text="Defect" to="/defect" />
        <NavButton text="Downtime" to="/downtime" />
      </div>
      <div class="menu-right">
        <span class="username">{{ username }}님</span>
        <NavButton text="MyPage" to="/signup" />
        <NavButton text="Logout" to="/" />
        <!-- <div class="hamburger" @click="toggleChat">
                <span></span>
                <span></span>
                <span></span>
              </div> -->
      </div>
    </nav>
     <!-- 채팅창 슬라이드 -->
    <!-- <div class="chat-panel" :class="{ open: chatOpen }">
      <h3>채팅방</h3>
      <div class="chat-messages">
        <div v-for="(msg, index) in messages" :key="index">{{ msg }}</div>
      </div>
      <input v-model="inputMsg" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요" />
      <button @click="sendMessage">전송</button>
    </div> -->
  </header>
</template>

<script>
import NavButton from './NavButton.vue'
import { ref , onMounted } from 'vue'
import { io } from 'socket.io-client'

export default {
  components: { NavButton },
  setup() {
    const username = ref('');
    const chatOpen = ref(false);
    const socket = io('http://192.168.200.238:3000'); // Node 서버 주소
    const messages = ref([]);
    const inputMsg = ref('');

    // 예시: 고정 방 이름
    const roomName = 'defaultRoom';

    onMounted(() => {
      username.value = sessionStorage.getItem("accountName") || '';

      // 방 참여
      socket.emit('joinRoom', { userId: username.value, roomName }, (res) => {
        console.log('방 참여 결과:', res);
      });

      // 서버에서 메시지 수신
      socket.on('message', (msg) => {
        messages.value.push(msg);
      });
    });

    function toggleChat() {
      chatOpen.value = !chatOpen.value;
    }

    function sendMessage() {
      if (inputMsg.value.trim() === '') return;

      // 서버에 메시지 전송
      socket.emit('chatMessage', { roomName, message: inputMsg.value });

      // 입력창 초기화
      inputMsg.value = '';
    }

    return { username, chatOpen, toggleChat, messages, inputMsg, sendMessage };
  }
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 왼쪽 정렬 */
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 10;
}

.logo {
  font-weight: bold;
  margin-right: 40px; /* 로고와 메뉴 사이 여백 */
}

.nav {
  display: flex;
  align-items: center;
  width: 100%;      /* 전체 헤더 폭을 사용 */
}

.menu-left {
  display: flex;
  gap: 15px;
}

.menu-right {
  display: flex;
  gap: 15px;
  margin-left: auto;
  align-items: center;
}

/* 햄버거 스타일 */
.hamburger {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 25px;
  height: 18px;
  cursor: pointer;
}
.hamburger span {
  display: block;
  height: 3px;
  background-color: white;
  border-radius: 2px;
}

/* 채팅 패널 */
.chat-panel {
  position: fixed;
  top: 60px; /* 헤더 아래 */
  right: -300px; /* 기본은 숨김 */
  width: 300px;
  height: calc(100% - 60px);
  background: #fff;
  box-shadow: -2px 0 5px rgba(0,0,0,0.2);
  transition: right 0.3s ease;
  display: flex;
  flex-direction: column;
  padding: 10px;
  z-index: 20;
}

.chat-panel.open {
  right: 0; /* 열리면 화면에 나타남 */
}

.chat-panel .chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 10px;
}
</style>
