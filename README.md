# FraudGuard Engine 🛡️

Sistema de detección de fraude transaccional en tiempo real, diseñado con arquitectura de microservicios.

## 🚀 Objetivo
Desarrollar un motor de reglas capaz de analizar transacciones financieras y emitir alertas automáticas basadas en patrones de comportamiento inusuales, geolocalización y límites de monto.

## 🛠️ Stack Tecnológico
- **Lenguaje:** Java 17+
- **Framework:** Spring Boot 3.x
- **Persistencia:** PostgreSQL & Hibernate
- **Gestión:** GitHub Projects (Kanban)

## 📋 Reglas de Detección (MVP)
- [ ] **Velocidad de Viaje:** Detección de compras en lugares distantes en tiempos imposibles.
- [ ] **Monto Inusual:** Alerta por transacciones que superan el 400% del promedio del usuario.
- [ ] **Ráfaga de Transacciones:** Bloqueo por múltiples intentos en menos de 60 segundos.
