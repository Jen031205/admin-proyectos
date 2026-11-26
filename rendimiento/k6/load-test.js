// ============================================================================
// PRUEBA DE CARGA CONTROLADA (k6) - PROYECTO INTEGRADOR admin-proyectos
// Evalúa: Latencia promedio, Percentil p95, Throughput (req/s) y Tasa de Error
// ============================================================================
import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('tasa_errores_http');
const backendLatency = new Trend('latencia_backend_ms', true);
const frontendLatency = new Trend('latencia_frontend_ms', true);

const FRONTEND_BASE = (__ENV.FRONTEND_URL || 'http://localhost:8080').replace(/\/+$/, '');
const BACKEND_BASE  = (__ENV.BACKEND_URL  || 'https://microservicioback-production.up.railway.app').replace(/\/+$/, '');

export const options = {
  stages: [
    { duration: '20s', target: 15 }, // Rampa de calentamiento a 15 usuarios virtuales (VUs)
    { duration: '40s', target: 30 }, // Carga sostenida con 30 usuarios concurrentes
    { duration: '15s', target: 0 },  // Enfriamiento gradual a 0 usuarios
  ],
  thresholds: {
    http_req_duration: ['p(95)<500', 'avg<250'], // Latencia p95 < 500ms y media < 250ms
    http_req_failed:   ['rate<0.01'],            // Tasa de error menor al 1%
    tasa_errores_http: ['rate<0.01'],
  },
};

export default function () {
  group('1. Microservicio Frontend (Ejercicio5Cliente)', function () {
    const resLogin = http.get(`${FRONTEND_BASE}/`);
    frontendLatency.add(resLogin.timings.duration);

    const okFront = check(resLogin, {
      'Frontend responde HTTP 200': (r) => r.status === 200,
      'Frontend latencia < 500ms':  (r) => r.timings.duration < 500,
    });
    errorRate.add(!okFront);
  });

  group('2. Microservicio Backend API REST (Ejercicio5MyAPIGestor)', function () {
    const endpoints = [
      '/buscarUsuario?id=196',
      '/buscarProyecto?id_proyecto=197',
      '/buscarTarea?id_tarea=199',
      '/buscarComentario?id=201',
      '/buscarAsignacion?id=1',
    ];

    for (const ep of endpoints) {
      const res = http.get(`${BACKEND_BASE}${ep}`);
      backendLatency.add(res.timings.duration);

      const okBack = check(res, {
        [`GET ${ep} status 200`]:      (r) => r.status === 200,
        [`GET ${ep} JSON válido`]:     (r) => r.body && r.body.length >= 2,
        [`GET ${ep} latencia < 500ms`]: (r) => r.timings.duration < 500,
      });
      errorRate.add(!okBack);
    }
  });

  sleep(1);
}
