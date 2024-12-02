<template>
    <div>
111
    </div>
</template>

<script>
    import {getExam} from "../../../api/tea"
    getExam().then(({data}) => {
        console.log(data)
    }) 
</script>

<style lang="scss" scoped>

</style>