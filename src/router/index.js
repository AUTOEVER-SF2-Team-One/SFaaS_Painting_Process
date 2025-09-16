import { createRouter, createWebHistory } from 'vue-router'
import Main from '@/views/Main.vue'
import Detail from '@/views/Detail.vue'
import Settings from '@/views/Settings.vue'
import Downtime from '@/views/Downtime.vue'
import Defect from '@/views/Defect.vue'
import Login from '@/views/Login.vue'

const routes = [
  { path: '/', component: Login, meta: { showHeader: false } },
  { path: '/main', component: Main, meta: { showHeader: true } },
  { path: '/detail', component: Detail, meta: { showHeader: true } },
  { path: '/settings', component: Settings, meta: { showHeader: true } },
  { path: '/downtime', component: Downtime, meta: { showHeader: true } },
  { path: '/defect', component: Defect, meta: { showHeader: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
