const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            totalBalance: 0,
            loans: [],
            cards: [],
            email: "",
            accounts: [],
            transactions: [],
            htmlEl: "",
            currentTheme: "",
            accountType: "",
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
                this.client.accounts.forEach(account => {
                    this.totalBalance = this.totalBalance + account.balance
                    account.transactions.forEach(transaction => this.transactions.push(transaction))
                })
                this.accounts = this.client.accounts
                this.loans = this.client.loans
                this.cards = this.client.cards
                this.email = this.client.email
                this.transactions.sort((a, b) => {
                    if (new Date(a.date) > new Date(b.date)) {
                        return -1
                    }
                    if (new Date(a.date) < new Date(b.date)) {
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
        addAccount() {
            axios.post('/api/clients/current/accounts', "email=" + this.email + "&type=" + this.accountType, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    swal.fire({
                        title: '¡Felicidades!',
                        text: 'Ya tenés tu nueva cuenta',
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
            chooseAccountType() {
                let swalWithBootstrapButtons = Swal.mixin({
                    customClass: {
                        confirmButton: 'btn btn-success',
                    },
                    buttonsStyling: false
                })
                swalWithBootstrapButtons.fire({
                    title: 'Selecciona el tipo de cuenta',
                    icon: 'question',
                    confirmButtonText: 'Crear cuenta',
                    color: 'white',
                    background: 'var(--table-transactions)',
                    reverseButtons: true,
                    html:
                        `<select name="accountType" id="swal-input1" class="form-select">
                        <option class="text-dark" value="" selected>--</option>
                        <option class="text-dark" value="AHORRO">AHORRO</option>
                        <option class="text-dark" value="CORRIENTE">CORRIENTE</option>
                    </select>`,
                }).then((result) => {
                    if (result.isConfirmed) {
                        this.accountType = document.getElementById('swal-input1').value 
                        console.log(this.accountType)
                        this.addAccount()
                    }
                })
            },
        logOut() {
            axios.post('/api/logout').then(response =>
                window.location.href = "/public/index.html"
            )
        },
        copyAccountNum(id) {
            let accountNumber = document.getElementById(id).innerHTML;
            navigator.clipboard.writeText(accountNumber.slice(4)).then(() => {
                console.log("copied")
            })
            const toast = swal.mixin({
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                color: 'white',
                background: 'var(--table-transactions)',
                timer: 2000,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })

            toast.fire({
                icon: 'success',
                title: 'Número de cuenta copiado'
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
