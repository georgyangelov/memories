export default class ImageUploader extends React.Component {
    constructor() {
        super();

        this.state = {};
    }

    render() {
        return this.state.file ? this.renderWithFile() : this.renderWithoutFile();
    }

    renderWithFile() {
        return <form className="image-uploader" onSubmit={this.upload.bind(this)}>
            <div className="metadata-panel">
                <input type="text" className="form-control" placeholder="Image name" />
                <input type="text" className="form-control" placeholder="Tags" />
            </div>

            <Dropzone className="preview" onDrop={this.onDrop.bind(this)} multiple={false}>
                <img src={this.state.file.preview} />
            </Dropzone>

            <div className="upload-panel">
                <button type="submit" className="btn btn-primary btn-lg">Upload</button>
                <button type="button" onClick={this.cancel.bind(this)} className="btn btn-lg">Cancel</button>
            </div>
        </form>;
    }

    renderWithoutFile() {
        return <div className="image-uploader">
            <Dropzone className="placeholder" onDrop={this.onDrop.bind(this)} multiple={false}>
                Drop a picture or click here to upload.
            </Dropzone>
        </div>;
    }

    onDrop(files) {
        this.setState({
            file: files[0]
        });
    }

    cancel() {
        this.setState({file: null});
    }

    upload() {

    }
}
