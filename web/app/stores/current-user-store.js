class CurrentUserStoreClass extends BaseStore {
    constructor() {
        super({
            accessToken: null,
            user: null
        });
    }

    loadStoredToken() {
        var accessToken = window.localStorage.getItem('accessToken');

        if (!accessToken) {
            return;
        }

        User.findByToken(accessToken, (error, user) => {
            if (error) {
                console.error('Invalid access token', error);
                return;
            }

            this.login(user, accessToken);
        });
    }

    login(user, accessToken) {
        this.setState({
            user: user,
            accessToken: accessToken
        });

        window.localStorage.setItem('accessToken', accessToken);
    }

    logout() {
        User.deauth(this.state.accessToken, ()=>{});

        this.setState({
            user: null,
            accessToken: null
        });

        window.localStorage.removeItem('accessToken');
    }
}

var CurrentUserStore = new CurrentUserStoreClass();
CurrentUserStore.name = 'CurrentUserStore';

export default CurrentUserStore;
