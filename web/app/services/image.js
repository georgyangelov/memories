export default class Image {
    static create(file, name, next) {
        request
            .put('/images')
            .use(Requests.auth)
            .field('name', name)
            .attach('image', file)
            .end(Requests.response(next));
    }
}
