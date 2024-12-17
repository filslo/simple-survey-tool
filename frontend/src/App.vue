<script setup>
</script>

<template>
    <layout>
        <router-view></router-view>
    </layout>
</template>

<script>
import Layout from './layouts/default.vue';

export default {
    components: {
        Layout,
    },
}
</script>
