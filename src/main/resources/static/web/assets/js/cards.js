const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            cards: [],
            debitCards: [],
            creditCards: [],
            totalBalance: 0,
            htmlEl: "",
            cardNumber:"",
            currentTheme: "",
            dateFormat: new Intl.DateTimeFormat('es-AR', { month: "numeric", year: "2-digit" }),
            moneyFormat: new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }),
        }
    },
    created() {
        this.loadData()
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
        loadData() {
            axios.get('/api/clients/current').then(response => {
                this.client = response.data
                this.cards = this.client.cards
                this.client.cards.forEach(card => {
                    if(card.type == 'DEBIT'){
                        this.debitCards.push(card)
                    } else{
                        this.creditCards.push(card)
                    }
                })
            })
        },
        darkMode(theme) {
            this.htmlEl.dataset.theme = theme
            localStorage.setItem("theme", theme)
        },
        logOut() {
            axios.post('/api/logout').then(response =>
                window.location.href = "/public/index.html"
            )
        },
        findDeactivateCard(card){
            this.cardNumber = card.number
            if(card.active){
                this.confirmDeactivateCard()
            } else{
                this.activateCard()
            }

        },
        confirmDeactivateCard() {
            let swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
                title: '¿Estás seguro?',
                text: "Una vez desactivada la tarjeta, no podés utilizarla. ¡Podés volver a activarla cuando desees!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: '¡Confirmar!',
                cancelButtonText: '¡Cancelar!',
                color: 'white',
                background: 'var(--table-transactions)',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.deactivateCard()
                } else if (
                    result.dismiss === Swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire({
                        title: '¡Cancelado!',
                        icon: 'error',
                        color: 'white',
                        background: 'var(--table-transactions)',
                        showConfirmButton: false
                    })

                }
            })
        },
        deactivateCard() {
            axios.patch('/api/cards/state', "number=" + this.cardNumber + "&state=deactivate", { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    swal.fire({
                        title: '¡Tarjeta desactivada!',
                        icon: "success",
                        showConfirmButton: false,
                        color: 'white',
                        background: 'var(--table-transactions)',
                        showClass: {
                            popup: 'animate__animated animate__fadeInDown'
                        },
                        hideClass: {
                            popup: 'animate__animated animate__fadeOutUp'
                        }
                    })
                    setTimeout(() => {
                        window.location.reload()
                    }, 1500)
                }).catch(response => {
                    swal.fire({
                        title: "¡Algo salió mal!",
                        text: "Volvé a intentarlo más tarde",
                        icon: "error",
                        color: 'white',
                        background: 'var(--table-transactions)',
                        button: "Ok",
                    });
                    
                })
                
            },
            activateCard() {
                axios.patch('/api/cards/state', "number=" + this.cardNumber + "&state=activate", { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        swal.fire({
                            title: '¡Tarjeta Activada!',
                            icon: "success",
                            showConfirmButton: false,
                            color: 'white',
                            background: 'var(--table-transactions)',
                            showClass: {
                                popup: 'animate__animated animate__fadeInDown'
                            },
                            hideClass: {
                                popup: 'animate__animated animate__fadeOutUp'
                            }
                        })
                        setTimeout(() => {
                            window.location.reload()
                        }, 1500)
                    }).catch(response => {
                        swal.fire({
                            title: "¡Algo salió mal!",
                            text: "Volvé a intentarlo más tarde",
                            icon: "error",
                            color: 'white',
                            background: 'var(--table-transactions)',
                            button: "Ok",
                        });
                        
                    })
                    
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

    }
},
).mount('#app')