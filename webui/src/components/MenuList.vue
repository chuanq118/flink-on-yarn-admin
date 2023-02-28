<template>
  <el-row class="top-logo" align="middle" justify="center">
    <el-image src="./flink-header-logo.svg" fit="contain"></el-image>
  </el-row>
  <el-menu class="top-menu" mode="horizontal" default-active="/" :router="true">
    <el-menu-item index="/">
      <span>FlinkOnYarn 任务管理</span>
    </el-menu-item>
    <el-menu-item index="/jar">
      <span>Jar 管理</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup lang="ts">
</script>

