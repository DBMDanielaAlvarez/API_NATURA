const mongoose = require('mongoose');

mongoose.connect("mongodb+srv://nanis_ar06:06Nanis11.@cluster0.shz3h.mongodb.net/naturadb?retryWrites=true&w=majority")
.then(db=> console.log("Conected MongoDB Atlas"))
.catch(err=> console.log(err));