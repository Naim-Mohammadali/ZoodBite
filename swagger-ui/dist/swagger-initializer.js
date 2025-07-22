window.onload = () => {
  window.ui = SwaggerUIBundle({
    url: "http://localhost:8080/openapi.yaml",
    dom_id: "#swagger-ui",
  });
};
