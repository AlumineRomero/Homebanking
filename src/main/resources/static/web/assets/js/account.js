const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            account: [],
            transactions: [],
            htmlEl: "",
            currentTheme: "",
            dateFormat: new Intl.DateTimeFormat('es-AR', { dateStyle: 'medium', timeStyle: 'medium' }),
            moneyFormat: new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }),
            startDate: "",
            endDate: "",
        }
    },
    created() {
        const queryString = location.search
        const params = new URLSearchParams(queryString)
        const idAccount = params.get("idAccount")
        this.loadData(idAccount)


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
        loadData(idAccount) {
            axios.get('/api/clients/current').then(response => {
                this.client = response.data
                this.account = this.client.accounts.find(account => account.id == idAccount)
                this.transactions = this.account.transactions
                this.transactions.sort((a, b) => {
                    if (a.id > b.id) {
                        return -1
                    }
                    if (a.id < b.id) {
                        return 1
                    } else {
                        return 0
                    }
                })
            })
        },
        darkMode(theme) {
            this.htmlEl.dataset.theme = theme
            localStorage.setItem("theme", theme)
        },
        confirmDeleteAccount() {
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
                confirmButtonText: '¡Confirmar!',
                cancelButtonText: '¡Cancelar!',
                color: 'white',
                background: 'var(--table-transactions)',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    this.deleteAccount()
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
        deleteAccount() {
            axios.patch('/api/accounts/delete', "accountNumber=" + this.account.number, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    swal.fire({
                        title: '¡Cuenta eliminada!',
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
                        window.location.href = "/web/accounts.html"
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
        logOut() {
            axios.post('/api/logout').then(response =>
                window.location.href = "/public/index.html"
            )
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
}).mount('#app')
