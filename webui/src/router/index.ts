import { createRouter, createWebHistory } from 'vue-router'
import JarManagement from "@/views/JarManagement.vue"


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'jar',
      component: JarManagement
    },
    {
      path: '/application',
      name: 'application',
      component: () => import('../views/ApplicationManagement.vue')
    }
  ]
})

export default router
