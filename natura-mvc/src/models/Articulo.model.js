const { Schema, model } = require("mongoose");
const articuloSchema = new Schema({
    clave:{
        type: String,
        require: true,
        unique: true
    },
    descripcion: String,
    modelo: String,
    preciocatalogo: Number,
    precioventa: Number,
    existencia: Number
},{
    versionKey:false,
    timestamps: true
});

module.exports = model('articulos', articuloSchema);