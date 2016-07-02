class ImageStoreClass extends BaseStore {
    constructor() {
        super({
            images: []
        });
    }

    reload() {
        Image.all((error, images) => {
            if (error) {
                console.log('Cannot load images', error);
                return;
            }

            this.setState({images: images});
        });
    }
}

var ImageStore = new ImageStoreClass();
ImageStore.name = 'ImageStore';

export default ImageStore;
