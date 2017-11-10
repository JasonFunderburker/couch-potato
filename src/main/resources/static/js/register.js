var RegisterForm = React.createClass({
    getInitialState: function () {
        return {login: "", password: ""};
    },
    updateLoginValue: function(evt) {
        this.setState({
            login: evt.target.value
        });
    },
    updatePasswordValue: function(evt) {
        this.setState({
            password: evt.target.value
        });
    },
    handleSubmit() {
        var self = this;
        $.ajax({
            url: 'http://localhost:4991/couch-potato/register',
            type: 'POST',
            contentType: 'application/json',
            data: '{"username": "'+ self.state.login +'",' +
            '"password":"'+self.state.password+'"}',
            success: function (result) {
                alert("Success added");
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.responseJSON.message);
            }
        });
    },
    render() {
        if (!this.props.active) return null;
        else
        return (
                <form className="form-signin">
                    <div className="form-group">
                        <label for="username">Create username</label>
                        <input type="username" className="form-control" id="username"
                               value={this.state.login} onChange={this.updateLoginValue} />
                    </div>
                    <div className="form-group">
                        <label for="password">Create password</label>
                        <input type="password" className="form-control" id="password"
                               value={this.state.password} onChange={this.updatePasswordValue}/>

                    </div>
                    <button type="submit" className="btn btn-lg btn-primary btn-block" onClick={this.handleSubmit}>Submit</button>
                </form>
        );
    }
});
var Register = React.createClass({

    checkAnyUserExist: function () {
        var self = this;
        $.ajax({
            url: "http://localhost:4991/couch-potato/register/check"
        }).then(function (data) {
            self.setState({anyUserExist: data.anyUserExist})
        });
    },

    handleRegister: function () {
        this.setState({
            needRegister : true,
            anyUserExist: true
        })
    },

    getInitialState: function () {
        return {anyUserExist: true, needRegister: false};
    },

    componentDidMount: function () {
        this.checkAnyUserExist();
    },
    render() {
        return (
            <div>
                {this.state.anyUserExist
                    ? null
                    : <button type="button" className="btn btn-lg btn-success btn-block"
                              onClick={this.handleRegister}>Register User</button>
                }
                <RegisterForm active={this.state.needRegister}/>
            </div>);
    }
});

ReactDOM.render(
    <Register />, document.getElementById('register')
);
