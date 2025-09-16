<template>
  <header class="header">
    <h1 class="logo">1Team</h1>
    <nav class="nav">
      <NavButton text="Main" to="/main" />
      <NavButton text="Detail" to="/detail" />
      <NavButton text="Defect" to="/defect" />
      <NavButton text="Downtime" to="/downtime" />
      <NavButton text="MyPage" to="/mypage" />
      <NavButton text="Logout" to="/" />
    </nav>
  </header>
</template>

<script>
import NavButton from './NavButton.vue'

export default {
  components: { NavButton },
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 10;
}
.nav {
  display: flex;
  gap: 15px;
}
.logo {
  font-weight: bold;
}
</style>
