import { useState } from "react";
import "./App.css";

interface ApiResponse {
  success: boolean;
  message: string;
  data: string | null;
}

function App() {
  const [username, setUsername] = useState("test_user");
  const [email, setEmail] = useState("testuser@example.com");
  const [password, setPassword] = useState("password123");

  const [response, setResponse] = useState<ApiResponse | null>(null);
  const [error, setError] = useState<ApiResponse | null>(null);

  const handleRegister = async () => {
    setResponse(null);
    setError(null);

    try {
      const res = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password })
      });

      const data: ApiResponse = await res.json();

      if (res.ok) {
        setResponse(data);
      } else {
        setError(data);
      }
    } catch {
      setError({
        success: false,
        message: "Network error",
        data: null
      });
    }
  };

  return (
    <div className="App">
      <h1>Issue Tracker Application</h1>

      <div style={{ maxWidth: "400px", margin: "0 auto", textAlign: "left" }}>
        <label>Username</label>
        <input value={username} onChange={e => setUsername(e.target.value)} />

        <label>Email</label>
        <input value={email} onChange={e => setEmail(e.target.value)} />

        <label>Password</label>
        <input
          type="password"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />

        <button onClick={handleRegister} style={{ marginTop: "15px" }}>
          Register
        </button>

        {response && (
          <div style={{ marginTop: "20px", color: "green" }}>
            <h3>Success</h3>
            <pre>{JSON.stringify(response, null, 2)}</pre>
          </div>
        )}

        {error && (
          <div style={{ marginTop: "20px", color: "red" }}>
            <h3>Error</h3>
            <pre>{JSON.stringify(error, null, 2)}</pre>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
