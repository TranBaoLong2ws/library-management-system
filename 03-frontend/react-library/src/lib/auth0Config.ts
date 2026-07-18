export const auth0Config = {
    clientId: "E1Umq1ExfWDqIgNOUHyw0vc704coKQyZ",
    domain: "tranbaolong2ws.us.auth0.com",
    audience: "https://api.luv2code.com",
    redirectUri: window.location.origin + "/callback",
    scope: 'openid profile email',
    pkce: true,
    disableHttpsCheck: true
}