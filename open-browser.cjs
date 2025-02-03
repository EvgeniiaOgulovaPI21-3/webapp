(async () => {
    const open = (await import('open')).default;
    open('http://localhost:8081').catch(console.error);
})();
