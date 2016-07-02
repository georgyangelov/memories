export default class User {
    static findByToken(accessToken, next) {
        request
            .get(`/users/${accessToken}`)
            .use(Requests.defaults)
            .end(Requests.response(next));
    }

    static create(name, email, password, next) {
        request
            .put('/users')
            .use(Requests.defaults)
               .send({name: name, email: email, password: password})
               .end(Requests.response(next));
    }

    static auth(email, password, next) {
        request
            .post('/users/auth')
            .use(Requests.defaults)
            .send({email: email, password: password})
            .end(Requests.response(next));
    }

    static deauth(accessToken, next) {
        request
            .post('/users/deauth')
            .use(Requests.defaults)
            .send({accessToken: accessToken})
            .end(Requests.response(next));
    }
}
