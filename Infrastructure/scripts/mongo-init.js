rs.initiate({
  _id: "rs0",
  members: [
    { _id: 0, host: "omnicure-mongo1-dev:27017", priority: 2 },
    { _id: 1, host: "omnicure-mongo2-dev:27018", priority: 1 }
  ]
});
