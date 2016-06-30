class CurrentUserStoreClass extends BaseStore {
    constructor() {
        super({
            accessToken: null,
            user: null
        });
    }

    login(user, accessToken) {
        this.setState({
            user: user,
            accessToken: accessToken
        });
    }
}

var CurrentUserStore = new CurrentUserStoreClass();
CurrentUserStore.name = 'CurrentUserStore';

export default CurrentUserStore;
