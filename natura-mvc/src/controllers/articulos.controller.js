const Articulo = require('../models/Articulo.model');
const articulosController = {};

articulosController.obtenerArticulos = async(req, res) =>{
    const articulos = await Articulo.find();
    res.json(articulos);
}

articulosController.obtenerArticulo=async(req,res)=>{
    const articulo=await Articulo.findOne({clave:req.params.cb});
    ///res.json(producto);
    if (articulo!=null)
        res.json(articulo);
    else
        res.json({status:"Not Found"});    
};

articulosController.insertarArticulo=async(req,res)=>{
    const articuloInsertado=new Articulo(req.body);
    await articuloInsertado.save();
    res.json({
        status:"Articulo insertado"
    });
};

articulosController.actualizarArticulo=async(req,res)=>{
    const resp = await Articulo.findOneAndUpdate({clave:req.params.cb},req.body);
    /*res.json({
        status:"Producto actualizado"
    });*/
    if(resp!=null)
        res.json({status:"Articulo Actualizado"});
    else
        res.json({status:"Not Found"});
};

articulosController.eliminarArticulo=async(req,res)=>{
    const resp = await Articulo.findOneAndDelete({clave:req.params.cb});
    /*res.json({
        status:"Producto eliminado"
    });*/

    if(resp!=null)
        res.json({status:"Articulo Eliminado"});
    else
        res.json({status:"Not Found"});
};
module.exports=articulosController;