/* eslint-disable no-restricted-globals */
let CASH_NAME = "AhrorPwa";
let filesToCashe = ["index.html", "offline.html"];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CASH_NAME).then((cache) => {
      return cache.addAll(filesToCashe);
    })
  );
});

self.addEventListener("fetch", (event) => {
  event.respondWith(
    caches.match(event.request).then(() => {
      return fetch(event.request).catch(() =>
        caches.match("offline.html")
      );
    })
  );
});
