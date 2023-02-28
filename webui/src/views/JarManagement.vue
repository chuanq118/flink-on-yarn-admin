<template>
<!--  <el-empty description="Not Allowed"></el-empty>-->
  <el-row justify="center" align="middle" style="width: 100%; min-height: 300px">
    <el-result
      icon="error"
      title="FORBIDDEN"
      sub-title="not allowed to upload jar.">
      <template #extra>
        <el-button type="primary"><a href="/ui" style="list-style: none;">返回</a></el-button>
      </template>
    </el-result>
  </el-row>
</template>

<script setup lang="ts">
</script>

<style scoped>

</style>
