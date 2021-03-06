const Miner = name => {
  const miner = {
    name,
    startWork: () => {
        setInterval(() => {
          const resources = ['ouro', 'prata', 'cobre', 'bronze'];
          const resource = resources[Math.floor(Math.random() * 10)] || 'ferro';
          console.log(`Minerando ${resource}...`)
        }, 3000);
    }
  }
  return miner;
};

module.exports = { Miner };