type LoginScreenProps = {
    onLogin: () => void;
};

export default function LoginScreen({onLogin}: Readonly<LoginScreenProps>) {
    return (
        <div className="login-screen">
            <div className="login-card">
                <h1 className="login-title">🎬 Movie Database</h1>
                <p className="login-subtitle">
                    Search movies by name, ID or plot. Sign in with GitHub to get started.
                </p>
                <button className="github-btn" onClick={onLogin}>
                    Login via GitHub
                </button>
            </div>
        </div>
    );
}
