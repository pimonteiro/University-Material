var Compositor = require('../models/compositor')

module.exports.listar = () => {
    return Compositor
                .find()
                .exec()
}