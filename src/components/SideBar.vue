<template>
  <div class="side-bar">
    <ul>
      <li v-for="(item, index) in menuItems" :key="index">
        <a href="#" @click.prevent="handleClick(item)">{{ item.icon }} {{ item.label }}</a>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'SideBar',
  props: {
    menuItems: {
      type: Array,
      required: true
    }
  },
  methods: {
    handleClick(item) {
      this.$emit('menu-clicked', item); // 부모에게 클릭된 메뉴 전달
    }
  }
}
</script>

<style scoped>
.side-bar {
  position: fixed;
  top: 60px; /* 네비게이션 바 아래에서 시작 */
  left: 0;
  width: 200px;
  height: calc(100vh - 60.5px); /* 네비게이션 바 높이를 제외한 전체 높이 */
  /* background: linear-gradient(180deg, #1e3c72, #2a5298);
   */
  background-color: white;
  color: black;
  /* box-shadow: 2px 0 5px rgba(0,0,0,0.1); */
  z-index: 1000;
}

.side-bar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.side-bar li {
  padding: 15px 20px;
}

.side-bar a {
  padding-top: 10%;
  color: black;

  text-decoration: none;
  display: block;
  transition: background-color 0.3s ease;
}

.side-bar a:hover {
  border: 1px solid blue;
}</style>