const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            cards:[],
            email:"",
            type:"",
            color:"",
            htmlEl: "",
            currentTheme: "",
            formInput: "",
            maxCards: false,
            }},
    created() {
        this.loadData()
        let radio = document.querySelector('#gold')
    },
    mounted() {
        this.htmlEl = document.getElementsByTagName("html")[0];
        this.currentTheme = localStorage.getItem("theme")
            ? localStorage.getItem("theme")
            : null;
        if (this.currentTheme) {
            this.htmlEl.dataset.theme = this.currentTheme;
        }


    },
methods: {
    loadData(){
        axios.get('/api/clients/current').then(response =>{
            this.client = response.data
            this.cards = this.client.cards

        })
    },
    createCard(){
        axios.post('/api/clients/current/cards',"type=" + this.type + "&color=" + this.color , {headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response => {
            swal.fire({
                title: '¡Felicidades!',
                text:'Ya tienes tu nueva tarjeta',
                icon: "success",
                color: 'white',
                background: 'var(--table-transactions)',
                showConfirmButton: false,
                showClass: {
                    popup: 'animate__animated animate__fadeInDown'
                },
                hideClass: {
                    popup: 'animate__animated animate__fadeOutUp'
                }
            })
            setTimeout(() => {
                window.location.href = "/web/cards.html"
            }, 1500)
        }).catch(response => {
            swal.fire({
                title: '¡Lo sentimos! Algo salió mal...',
                icon: "error",
                color: 'white',
                background: 'var(--table-transactions)',
                showClass: {
                    popup: 'animate__animated animate__fadeInDown'
                },
                hideClass: {
                    popup: 'animate__animated animate__fadeOutUp'
                }
            })
            })
            
    },
    darkMode(theme) {
        this.htmlEl.dataset.theme = theme
        localStorage.setItem("theme", theme)
    },
    areYouSure() {
        let swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
        })

        swalWithBootstrapButtons.fire({
            title: '¿Estás seguro?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '¡Cerrar sesión!',
            cancelButtonText: '¡Me quedo!',
            color: 'white',
            background: 'var(--table-transactions)',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.logOut()
            } else if (
                result.dismiss === Swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire({
                    title: '¡Qué alegría!',
                    text: 'Nos alegra mucho que te quedes acá',
                    icon: 'success',
                    color: 'white',
                    background: 'var(--table-transactions)',
                    showConfirmButton: false
                })

            }
        })
    }
},
},
).mount('#app')